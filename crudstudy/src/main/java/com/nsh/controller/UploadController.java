package com.nsh.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nsh.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		
		log.info("Ajax로 파일 업로드");
	}
	
	/**
	 * 썸네일 데이터 전송
	 * byte[]로 이미지 파일 데이터를 전송할 때 MIME 타입이 파일 종류에 따라 달라지는데
	 * 이를 probeContentType( )을 이용해서 MIME 타입 데이터를 Http 헤더 메세지에 포함할 수 있게 처리
	 */
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName) {
		
		log.info("파일 이름: " + fileName);
		
		File file = new File("D:\\ImageRepository\\" + fileName);
		
		log.info("파일: " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 중복된 파일 이름 처리
	 * 1. 밀리세컨드까지 구분해서 이름을 생성하는 방법
	 * 2. UUID를 이용해 중복 발생할 가능성이 거의 없는 문자열 생성하는 방법
	 * 2번 방법 같은 경우 너무 많은 파일이 있는 경우 속도 저하와 개수 제한 문제때문에
	 * 년/월/일 단위로 폴더를 생성해서 파일을 저장하면 됨
	 */
	
	/**
	 * 년/월/일 파일 생성
	 */
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	/**
	 * 서버에 업로드 되기 전 이미지 파일인지 검사한다.
	 */
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Ajax가 준 formData를 여기서 받는다.
	 * 404에러를 뱉는 이유는 @RestController로 작성되지 않았다면
	 * @ResponseBody 어노테이션을 메소드에 붙여서 JSON 또는 XML타입으로
	 * 객체를 리턴하도록 해야된다.
	 * 
	 * 파일 업로드 고려 사항
	 * 1. 동일한 이름으로 파일이 업로드 될 때, 기존 파일이 사라짐
	 * 2. 이미지 파일 경우 원본 파일 용량이 크면 섬네일 이미지를 생성해서 처리해야됨
	 * 3. 이미지 파일과 일반 파일을 구분해서 처리해야됨
	 * 4. 첨부파일 공격에 대비하기 위해 확장자를 제한해야됨
	 */
	@PostMapping(value = "/uploadAjaxAction")
	@ResponseBody
	/**
	 * 브라우저에게 업로드된 파일을 가지고 피드백주기 위해
	 * AttachFileDTO의 리스트를 반환하는 구조로 변경 
	 */
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		log.info("Ajax 포스트 업데이트");
		/**
		 * AttachFileDTO를 받는 List 선언
		 */
		List<AttachFileDTO> list = new ArrayList();
		String uploadFolder = "D:\\ImageRepository";
		/**
		 * getFolder() 결과를 초기화한 변수
		 */
		String uploadFolderPath = getFolder();
		/**
		 * 폴더 만들기
		 */
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("업로드 path: " + uploadPath);
		
		/**
		 * 해당 경로가 존재하지 않으면 폴더를 만들게됨
		 */
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		/**
		 * 받은 formData를 순서대로 꺼냄
		 */
		for (MultipartFile multipartFile : uploadFile) {
			log.info("===================");
			log.info("업로드 파일 이름: " + multipartFile.getOriginalFilename());
			log.info("업로드 파일 크기: " + multipartFile.getSize());
			
			//AttachFileDTO 객체화
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//인터넷 익스플로러 설정
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf('\\') + 1);
			
			log.info("오직 파일 이름만: " + uploadFileName);
			//AttachFileDTO에 setter로 값 변경
			attachDTO.setFileName(uploadFileName);
			/**
			 * 년/월/일 폴더를 만들어서 파일을 분리했지만,
			 * 아직 파일이름이 동일하면 기존 업로드 된 파일을 지우기 때문에
			 * UUID를 사용함
			 */
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			log.info("UUID 적용 후 파일 이름: " + uploadFileName);
			
			//업로드 폴더, 파일 이름 파라미터로 받아서 객체화
			//File saveFile = new File(uploadFolder, uploadFileName);
			
			//년/월/일로 구분했기 때문에 uploadFolde 대신 uploadPath를 받는다.
			//File saveFile = new File(uploadPath, uploadFileName);
			
			try {
				//년/월/일로 구분했고, 업로드하는 파일이 이미지인지 확인
				File saveFile = new File(uploadPath, uploadFileName);
				//위에서 지정한 파일을 전송
				multipartFile.transferTo(saveFile);
				//AttachFileDTO에 setter로 값 변경
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				//이미지 파일인 경우
				if (checkImageType(saveFile)) {
					attachDTO.setImage(true);//이미지 파일이 맞으니까 setter로 true 설정
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				//리스트에 만들어둔 객체 추가
				list.add(attachDTO);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		//메서드 반환
		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
	}
	
	/**
	 * 첨부파일 다운로드
	 * 테스트 후 정상 작동한다면 HttpHeaders 객체를 이용해서 다운로드 시 파일의 이름을 처리
	 * 
	 * IE브라우저와 같이 서비스 한다면 디바이스 정보를 알 수 있는 User-Agent 값을 이용
	 * 
	 * downFile( )이 User-Agent정보를 파라미터로 수집한다.
	 */
	@GetMapping(value = "/download")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
		
		log.info("다운로드 파일: " + fileName);
		
		Resource resource = new FileSystemResource("D:\\ImageRepository\\" + fileName);
		/**
		 * 해당 이미지파일이 존재하지 않음
		 */
		if (resource.exists() == false) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		
		log.info("리소스: " + resource);
		
		String resourceName = resource.getFilename();
		
		/**
		 * 다운로드 할때 UUID 이름 지우고 다운로드 할 수 있게끔 조치
		 */
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
		
		log.info("오리지널 이름: " + resourceOriginalName);
		
		HttpHeaders headers = new HttpHeaders();
		try {
			/**
			 * IE 브라우저 서비스
			 */
			String downloadName = null;
			
			if (userAgent.contains("Trident")) {
				log.info("IE 브라우저");
				
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			} else if (userAgent.contains("Edge")) {
				
				log.info("Edge 브라우저");
				
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				
				log.info("Edge 이름: " + downloadName);
			} else {
				log.info("Chrome 브라우저");
				
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			/**
			 * 파일 이름이 한글인 경우 저장할 때 깨지는 문제를 막기 위해서
			 */
			headers.add("Content-Disposition", "attachment; filename=" +  downloadName);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	/**
	 * ajax에서 전달된 파일 이름과 파일 타입을 가지고 폴더에서 파일을 삭제합니다.
	 */
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type) {
		
		log.info("deleteFile: " + fileName);
		
		File file;
		
		try {
			file = new File("D:\\ImageRepository\\" + URLDecoder.decode(fileName, "UTF-8"));
			
			log.info("file: " + file);
			
			file.delete();
			
			if (type.equals("image")) {
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				
				log.info("largeFileName: " + largeFileName);
				
				file = new File(largeFileName);
				
				file.delete();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
		
	}
}

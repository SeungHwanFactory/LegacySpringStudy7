package com.nsh.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nsh.domain.BoardAttachVO;
import com.nsh.mapper.BoardAttachMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {

	
	private BoardAttachMapper attachMapper;
	
	@Autowired
	public FileCheckTask (BoardAttachMapper attachMapper) {
		this.attachMapper = attachMapper;
	}
	
	//어제 폴더 구하기
	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, -1);
		
		String str = sdf.format(cal.getTime());
		
		return str.replace("-", File.separator);
	}
	
	
	/**
	 * 매일 새벽2시에 동작
	 * attachMapper를 이용해 어제 날짜로 보관된 모든 첨부파일을 가져오고
	 * 데이터베이스에서 가져온 목록은 BoardAttachVO 타입 객체기에 나중에 비교를 위해
	 * java.nio.Paths 목록으로 변환했음
	 * 
	 * 이미지 파일인 경우 섬네일 파일도 목록에 필요하기에
	 * 별도로 처리해서 해당 일의 예상 파일 목록을 완성했음
	 * 
	 * 코드에서 fileListPaths라는 이름의 변수로 처리함
	 * 
	 * 데이터베이스에 있는 파일들의 준비가 끝나면
	 * 실제 폴더에 있는 파일들의 목록에서 데이터베이스에는
	 * 없는 파일들을 찾아서 목록으로 준비함
	 * 
	 * 결과는 removFiles 변수에 담아서 처리했음
	 * 최종적으로 삭제 대상이 되는 파일들을 삭제함
	 * 
	 */
	@Scheduled(cron="0 0 2 * * *")
	public void checkFiles() throws Exception {
		log.warn("파일 체크 테스크 런..................................");
		log.warn("===========================");
		
		//데이터 베이스에 들어있는 파일 목록
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();
		
		//데이터베이스 파일 목록이 있는 디렉토리에서 파일을 확인할 준비 됐다.
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("D:\\ImageRepository", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());
		
		//이것은 썸네일 파일
		fileList.stream().filter(vo -> vo.isFileType() == true)
			.map(vo -> Paths.get("D:\\\\ImageRepository", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
			.forEach(p -> fileListPaths.add(p));
		
		log.warn("=============================");
		
		fileListPaths.forEach(p -> log.warn(p));
		
		//어제 디렉토리 파일
		File targetDir = Paths.get("D:\\ImageRepository", getFolderYesterDay()).toFile();
		
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

		log.warn("=============================");
		
		for (File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();
		}
	}
}

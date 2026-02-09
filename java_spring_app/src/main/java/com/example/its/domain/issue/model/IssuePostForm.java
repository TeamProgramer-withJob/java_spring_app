package com.example.its.domain.issue.model;

import org.springframework.web.multipart.MultipartFile;

import com.example.its.domain.annot.ValidateFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IssuePostForm {
	@NotBlank(message = "必須項目です。")
	@Size(max = 256)
	private String summary;
	
	@Size(max = 256)
	private String description;
	
	@ValidateFile( maxSize = 10*1024*1024, types = {"image/png", "image/jpeg"}, message = "Please choose a jpeg or png image file.", 
			messages = {"ファイルサイズは10MBを超えています","ファイルフォーマットはpngとjpegのみサポートしています。"})
	private MultipartFile file;
}

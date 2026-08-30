package com.example.its.domain.mem.model;

import org.springframework.web.multipart.MultipartFile;

import com.example.its.domain.annot.ValidateFile;
import com.example.its.domain.mem.entity.EntityMemImage;
import com.example.its.domain.mem.entity.EntityMemory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MemoryForm {
	private int id;
	private int userId;
	private String act;
	
	@NotBlank(message = "必須項目です。")
	@Size(max = 128)
	private String title;
	
	@Size(max = 512)
	private String details;
	
	@ValidateFile( maxSize = 10*1024*1024, types = {"image/png", "image/jpeg"}, message = "Please choose a jpeg or png image file.", 
			messages = {"ファイルサイズは10MBを超えています","ファイルフォーマットはpngとjpegのみサポートしています。"})
	private MultipartFile file;
	
	private int imageId;
	private String filename;
	
	public MemoryForm(EntityMemory entity, EntityMemImage image) {
		id = entity.getId();
		title = entity.getTitle();
		details = entity.getDetails();
		this.imageId = image.getId();
		this.filename = image.getFilename();
	}
	public MemoryForm(EntityMemory entity) {
		id = entity.getId();
		title = entity.getTitle();
		details = entity.getDetails();
	}
}

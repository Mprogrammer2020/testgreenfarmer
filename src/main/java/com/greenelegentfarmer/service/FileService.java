package com.greenelegentfarmer.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

	private final String filePath="uploads/static";
	//private final String filePath="../uploads/static";
	protected final Path root = Paths.get(filePath);

	@PostConstruct
	public void init() throws IOException {
		Files.createDirectories(root);
	}

	public String save(MultipartFile file,String appendPath) throws IOException {
		String fileName=file.getOriginalFilename().replaceAll(" ", "-");
		
		Files.deleteIfExists(this.root.resolve(appendPath+"/"+fileName));
		
		Files.createDirectories(Paths.get(filePath+"/"+appendPath));
		
		Files.copy(file.getInputStream(), this.root.resolve(appendPath+"/"+fileName));
		
		return "static/"+appendPath+"/"+fileName;
	}

	public Resource load(String filename) throws MalformedURLException {

		Path file = root.resolve(filename);
		Resource resource = new UrlResource(file.toUri());

		if (resource.exists() || resource.isReadable())
			return resource;
		else
			throw new RuntimeException("Could not read the file!");

	}

	public void delete(String filePath) throws IOException {
		FileSystemUtils.deleteRecursively(new File(filePath+"/"+filePath));
	}

	public Stream<Path> loadAll() throws IOException {
		//return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		return null;
	}
}

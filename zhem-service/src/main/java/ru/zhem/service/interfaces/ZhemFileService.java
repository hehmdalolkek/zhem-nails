package ru.zhem.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.zhem.entity.ZhemFile;

import java.io.IOException;
import java.util.List;

public interface ZhemFileService {

    List<ZhemFile> createFiles(List<MultipartFile> multipartFiles) throws IOException;

    ZhemFile createFile(MultipartFile multipartFile) throws IOException;

    void deleteFiles(List<ZhemFile> files) throws IOException;

    void deleteFile(ZhemFile file) throws IOException;

}

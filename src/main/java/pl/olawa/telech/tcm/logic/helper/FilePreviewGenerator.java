package pl.olawa.telech.tcm.logic.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import pl.olawa.telech.tcm.model.exception.TcmException;

@Component
public class FilePreviewGenerator {

	@Value("${tcm.repo.previewSize}")
	private int previewSize;
	
	private static final Set<String> availableTypes;

	static {
		availableTypes = new TreeSet<>();
		availableTypes.add("image/jpeg");
		availableTypes.add("image/png");
		availableTypes.add("image/gif");
		availableTypes.add("image/bmp");
		availableTypes.add("application/pdf");
		availableTypes.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		availableTypes.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");	
	}

	public boolean isAvailable(String mimeType) {
		return availableTypes.contains(mimeType);
	}

	public Pair<String, byte[]> createPreview(MultipartFile file) throws IOException  {
		switch (file.getContentType()) {
			case "image/jpeg":
			case "image/png":
			case "image/gif":
			case "image/bmp":
				return processJpg(file.getInputStream());
			
			case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":		
				return processDocx(file.getInputStream());
				
			case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
				return processXlsx(file.getInputStream());
				
			case "application/pdf":
				return Pair.of("application/pdf", file.getBytes());
				
			default:
				throw new TcmException("Mime type " + file.getContentType() + " is not available.");
		}
	}
	
	private Pair<String, byte[]> processJpg(InputStream is) throws IOException {
		BufferedImage img = ImageIO.read(is);
		
		double scale = (double)img.getWidth() / img.getHeight();
		int thumbWidth = previewSize;
		int thumbHeight = (int) Math.round(previewSize / scale);
		if(thumbWidth > previewSize) {
			thumbHeight = previewSize;
			thumbWidth = (int) Math.round(previewSize * scale);
		}
		
		BufferedImage thumb = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
		thumb.createGraphics().drawImage(img.getScaledInstance(thumbWidth, thumbHeight, BufferedImage.SCALE_SMOOTH), 0, 0, null);
		
		var bos = new ByteArrayOutputStream();
		ImageIO.write(img, "jpg", bos);
		return Pair.of("image/jpg", bos.toByteArray());
	}
	
	private Pair<String, byte[]> processDocx(InputStream is) throws IOException {
		var bos = new ByteArrayOutputStream();
		
		IConverter converter = LocalConverter.builder().build();
        converter
        	.convert(is).as(DocumentType.DOCX)
        	.to(bos).as(DocumentType.PDF)
        	.execute();

        bos.close();
        return Pair.of("application/pdf", bos.toByteArray());
	}
	
	private Pair<String, byte[]> processXlsx(InputStream is) throws IOException {
		var bos = new ByteArrayOutputStream();
		
		IConverter converter = LocalConverter.builder().build();
        converter
        	.convert(is).as(DocumentType.XLSX)
        	.to(bos).as(DocumentType.PDF)
        	.execute();

        bos.close();
        return Pair.of("application/pdf", bos.toByteArray());
	}
	
}

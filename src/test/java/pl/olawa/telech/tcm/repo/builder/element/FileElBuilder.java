package pl.olawa.telech.tcm.repo.builder.element;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class FileElBuilder extends ElementBuilder {
	
	Integer size = 100;				
	String mimeType = "application/pdf";			
	Integer previewSize = 10;		
	String previewMimeType = "image/jpg";			
	
	@Override
	public FileEl build() {
		var obj = new FileEl();
		super.fill(obj);
		obj.setSize(size);
		obj.setMimeType(mimeType);
		obj.setPreviewSize(previewSize);
		obj.setPreviewMimeType(previewMimeType);
		return obj;	
	}
}

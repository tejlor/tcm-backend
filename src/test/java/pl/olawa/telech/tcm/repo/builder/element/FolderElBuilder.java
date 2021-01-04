package pl.olawa.telech.tcm.repo.builder.element;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class FolderElBuilder extends ElementBuilder<FolderEl> {
	
	String icon = "icon";						
	
	@Override
	public FolderEl build() {
		var obj = new FolderEl();
		super.fill(obj);
		obj.setIcon(icon);
		return obj;	
	}
}

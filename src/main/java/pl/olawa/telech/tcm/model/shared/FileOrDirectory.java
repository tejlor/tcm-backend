package pl.olawa.telech.tcm.model.shared;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.element.Directory;
import pl.olawa.telech.tcm.model.entity.element.File;
import pl.olawa.telech.tcm.model.interfaces.Loggable;

@Getter @Setter
@NoArgsConstructor
public class FileOrDirectory implements Comparable<FileOrDirectory>, Loggable {
	
	private String ref;
	private String name;
	private boolean isFile;
	
	
	public FileOrDirectory(File file) {
		ref = file.getRef().toString();
		name = file.getName();
		isFile = true;
	}
	
	public FileOrDirectory(Directory dir) {
		ref = dir.getRef().toString();
		name = dir.getName();
		isFile = true;
	}

	@Override
	public int compareTo(FileOrDirectory other) {
		if(this.isFile != other.isFile) { 	// directories first
			return this.isFile ? -1 : 1;
		}
		else {								// then by name 
			return this.name.compareToIgnoreCase(other.name);
		}
	}
}

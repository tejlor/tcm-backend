package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.model.dto.AbstractDto;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repo.model.entity.AccessRight;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AccessRightDto extends AbstractDto implements Comparable<AccessRightDto> {

	int elementId;
	int orderNo;
	boolean forAll;
	Set<Integer> userIds;
	Set<Integer> userGroupIds;
	boolean canCreate;
	boolean canRead;
	boolean canUpdate;
	boolean canDelete;
	
	public AccessRightDto(AccessRight model) {
		super(model);
		userIds = model.getUsers().stream().map(u -> u.getId()).collect(Collectors.toSet());
		userGroupIds = model.getUserGroups().stream().map(ug -> ug.getId()).collect(Collectors.toSet());
	}

	@Override
	public AbstractEntity toModel() {
		var model = new AccessRight();
		fillModel(model);
		model.setUsers(userIds.stream().map(id -> new User(id)).collect(Collectors.toSet()));
		model.setUserGroups(userGroupIds.stream().map(id -> new UserGroup(id)).collect(Collectors.toSet()));
		return model;
	}

	public static List<AccessRightDto> toDtoList(List<AccessRight> list){
		return toDtoList(AccessRight.class, AccessRightDto.class, list);
	}

	@Override
	public int compareTo(AccessRightDto other) {
		return Integer.compare(this.orderNo, other.getOrderNo());
	}
}

package mapper;

import dto.BorrowDto;
import model.Borrow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper extends EntityMapper<BorrowDto, Borrow> {
}

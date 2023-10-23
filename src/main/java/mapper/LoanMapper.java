package mapper;

import dto.LoanDto;
import model.Loan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper extends EntityMapper<LoanDto, Loan> {
}

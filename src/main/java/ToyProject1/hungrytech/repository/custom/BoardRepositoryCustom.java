package ToyProject1.hungrytech.repository.custom;

import ToyProject1.hungrytech.boardDto.BoardSearchCondition;
import ToyProject1.hungrytech.boardDto.BulletinBoardInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BulletinBoardInfo> search(Pageable pageable,
                                   BoardSearchCondition boardSearchCondition);
}


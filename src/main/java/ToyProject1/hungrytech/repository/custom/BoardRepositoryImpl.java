package ToyProject1.hungrytech.repository.custom;

import ToyProject1.hungrytech.boardDto.BoardSearchCondition;
import ToyProject1.hungrytech.boardDto.BulletinBoardInfo;
import ToyProject1.hungrytech.boardDto.QBulletinBoardInfo;
import ToyProject1.hungrytech.entity.board.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static ToyProject1.hungrytech.entity.board.QBoard.*;
import static ToyProject1.hungrytech.entity.member.QMember.*;
import static org.springframework.util.StringUtils.*;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<BulletinBoardInfo> search(Pageable pageable,
                                          BoardSearchCondition searchCondition) {

        List<BulletinBoardInfo> content = queryFactory
                .select(new QBulletinBoardInfo(board))
                .from(board)
                .join(board.member, member)
                .fetchJoin()
                .where(
                        isServiceable(searchCondition)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdDate.desc())
                .fetch();

        //카운트 쿼리 최적화
        JPAQuery<Board> countQuery = queryFactory
                .select(board)
                .from(board)
                .join(board.member, member)
                .fetchJoin()
                .where(
                        isServiceable(searchCondition)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private BooleanExpression isServiceable(BoardSearchCondition searchCondition) {
        String condition = hasText(searchCondition.getCondition()) ?
                searchCondition.getCondition() : "";

        switch (condition) {
            case "title" :
                return titleEq(searchCondition);
            case "content" :
                return contentEq(searchCondition);
            case "accountId" :
                return memberAccountIdEq(searchCondition);
            case  "titleAndContent" :
                return titleAndContentEq(searchCondition);
        }

        return null;
    }

    private BooleanExpression titleEq(BoardSearchCondition searchCondition) {
        return hasText(searchCondition.getSearchText()) ?
                board.title
                        .contains(searchCondition.getSearchText()) : null;
    }

    private BooleanExpression contentEq(BoardSearchCondition searchCondition) {
        return hasText(searchCondition.getSearchText()) ?
                board.content
                        .contains(searchCondition.getSearchText()) : null;
    }

    private BooleanExpression memberAccountIdEq(BoardSearchCondition searchCondition) {
        return hasText(searchCondition.getSearchText()) ?
                member.accountId
                        .contains(searchCondition.getSearchText()) : null;
    }

    private BooleanExpression titleAndContentEq(BoardSearchCondition searchCondition) {
        return hasText(searchCondition.getSearchText()) ?
                board.title
                        .contains(searchCondition.getSearchText())
                        .or(board.content
                                .contains(searchCondition.getSearchText())) : null;
    }
}

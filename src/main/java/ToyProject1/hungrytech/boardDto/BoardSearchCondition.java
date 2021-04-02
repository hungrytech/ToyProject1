package ToyProject1.hungrytech.boardDto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class BoardSearchCondition {


    @Setter(AccessLevel.NONE)
    private Map<String, String> conditionList = new HashMap<>(){{
        put("제목", "title");
        put("내용", "content");
        put("글쓴이(아이디)", "accountId");
        put("제목+내용", "titleAndContent");
    }};

    private String condition;

    private String searchText;



}

package dku.cloudcomputing.surveyresultserver.exception.member;

import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;

public class NotMatchMemberException extends ClientOccurException {
    public NotMatchMemberException() {
        this("요청하는 회원과 요청 정보의 회원이 일치하지 않습니다");
    }

    public NotMatchMemberException(String message) {
        super(message);
    }
}

package dku.cloudcomputing.surveyresultserver.exception.survey;

public class NoSuchSurveyException extends RuntimeException {
    public NoSuchSurveyException() {
        new NoSuchSurveyException("해당되는 설문이 없습니다");
    }

    public NoSuchSurveyException(String message) {
        super(message);
    }
}

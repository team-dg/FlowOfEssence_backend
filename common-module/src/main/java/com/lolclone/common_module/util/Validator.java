package com.lolclone.common_module.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.lolclone.common_module.commonexception.UnexpectedException;
import com.lolclone.common_module.commonexception.ValidException;

public final class Validator {
    private Validator() {

    }

    /**
     * 문자열이 null인지 공백인지 검사
     * 
     * @param input             검증할 문자열
     * @param fieldName         예외 메시지에 출력할 필드명
     * @throws ValidException   input이 null 또는 공백일 경우
     */
    public static void notBlank(String input, String fieldName) {
        if(input == null || input.isBlank()) {
            throw new ValidException("%s은 null 또는 공백이 될 수 없습니다.".formatted(fieldName));
        }
    }

    /**
     * 문자열의 최대 길이 검증 null 값은 무시, 최대 길이가 0이하면 예외 던짐
     * @param input           검증할 문자열
     * @param maxLength       검증할 문자열의 최대 길이
     * @param fieldName       예외 메시지를 출력할 필드명
     * @throws UnexpectedException  최대 길이가 0이하일 경우
     * @throws ValidException       input의 길이가 maxLength 초과일 경우
     */
    public static void maxLength(String input, int maxLength, String fieldName) {
        if(maxLength <= 0) {
            throw new UnexpectedException("최대 길이는 0 이하일 수 없습니다.");
        }
        if(input == null) {
            return;
        }

        if(input.length() > maxLength) {
            throw new ValidException("%s의 길이는 %d자 이하로 입력해야 합니다.".formatted(fieldName, maxLength));
        }  
    }

    /**
     * 문자열의 최소 길이를 검증, null 값은 무시, 최소 길이가 0이하면 예외를 던짐
     * 
     * @param input         검증할 문자열
     * @param minLength     검증할 문자열의 최소 길이
     * @param fieldName     예외 메시지를 출력할 필드명
     * @throws UnexpectionException    maxLength 가 0 이하이면
     * @throws ValidException          input의 길이가 minLength 미만이면
     */
    public static void minLength(CharSequence input, int minLength, String fieldName) {
        if(minLength <= 0) {
            throw new UnexpectedException("최소 길이는 0 이하일 수 없습니다.");
        }
        if(input == null) {
            return;
        }
        if(input.length() < minLength) {
            throw new ValidException("%s의 길이는 %d자 이상으로 입력해야 합니다.".formatted(fieldName, minLength));
        }
    }

    /**
     * 객체가 null인지 검사
     *
     * @param object    검증할 객체
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException object가 null일 경우
     */
    public static void notNull(Object object, String fieldName) {
        if(object == null) {
            throw new ValidException("%s은 null이 될 수 없습니다.".formatted(fieldName));
        }
    }

    /**
     * 값의 최대 값을 검증
     *
     * @param value     검증할 값
     * @param maxValue  검증할 값의 최대 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 maxValue 초과하면
     */
    public static void maxValue(int value, int maxValue, String fieldName) {
        if(value > maxValue) {
            throw new ValidException("%s은 %d 이하로 입력해야 합니다.".formatted(fieldName, maxValue));
        }
    }

    /**
     * 값의 최소 값을 검증
     *
     * @param value     검증할 값
     * @param minValue  검증할 값의 최소 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 minValue 미만이면
     */
    public static void minValue(long value, long minValue, String fieldName) {
        if (value < minValue) {
            throw new ValidException("%s은 %d 이상으로 입력해야 합니다.".formatted(fieldName, minValue));
        }
    }

    /**
     * 값의 최소 값을 검증
     *
     * @param value     검증할 값
     * @param minValue  검증할 값의 최소 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 minValue 미만이면
     */
    public static void minValue(double value, double minValue, String fieldName) {
        if (value < minValue) {
            throw new ValidException("%s은 %d 이상으로 입력해야 합니다.".formatted(fieldName, minValue));
        }
    }

    /**
     * 값이 음수인지 검증합니다.
     *
     * @param value     검증할 값
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException value가 음수이면
     */
    public static void notNegative(int value, String fieldName) {
        if (value < 0) {
            throw new ValidException("%s은 음수가 될 수 없습니다.".formatted(fieldName));
        }
    }

    /**
     * 컬렉션의 최대 size를 검증
     *
     * @param collection 검증할 컬렉션
     * @param maxSize    검증할 컬렉션의 최대 원소 수
     * @param fieldName  예외 메시지에 출력할 필드명
     * @throws UnexpectedException 최대 크기가 0 이하이면
     * @throws ValidException      collecion의 size가 maxSize를 초과하면
     */
    public static void maxSize(Collection<?> collection, int maxSize, String fieldName) {
        if (maxSize <= 0) {
            throw new UnexpectedException("최대 size는 0 이하일 수 없습니다.");
        }
        if (collection == null || collection.size() > maxSize) {
            throw new ValidException("%s의 size는 %d 이하여야 합니다.".formatted(fieldName, maxSize));
        }
    }

    /**
     * 컬렉션의 최소 size를 검증
     *
     * @param collection 검증할 컬렉션
     * @param minSize    검증할 컬렉션의 최소 원소 수
     * @param fieldName  예외 메시지에 출력할 필드명
     * @throws UnexpectedException 최소 크기가 0 이하이면
     * @throws ValidException      collecion의 size가 minSize 미만이면
     */
    public static void minSize(Collection<?> collection, int minSize, String fieldName) {
        if (minSize <= 0) {
            throw new UnexpectedException("최소 size는 0 이하일 수 없습니다.");
        }
        if (collection == null || collection.size() < minSize) {
            throw new ValidException("%s의 size는 %d 이상이어야 합니다.".formatted(fieldName, minSize));
        }
    }

    /**
     * 리스트에 중복이 있는지 검사, HashSet을 사용하여 중복을 검사하므로, 리스트의 원소 타입은 반드시 equals, hashCode 메서드를 구현해야 함
     *
     * @param list      검증할 리스트
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException 리스트에 중복이 있으면
     */
    public static void notDuplicate(List<?> list, String fieldName) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (new HashSet<>(list).size() != list.size()) {
            throw new ValidException("%s에 중복된 값이 있습니다.".formatted(fieldName));
        }
    }

    /**
     * 이메일 형식을 검증
     * @param email 검증할 이메일
     * @param fieldName 예외 메시지에 출력할 필드명
     * @throws ValidException 이메일 형식이 올바르지 않으면
     */
    public static void email(String email, String fieldName) {
        if (!email.matches("^[a-zA-Z0-9!@#]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidException("%s은 올바른 이메일 형식이 아닙니다.".formatted(fieldName));
        }
    }
}

package com.lolclone.chatdomain.domain.notification;

import java.util.Map;

import com.lolclone.chatdomain.domain.JsonConverter;
import com.lolclone.chatdomain.exception.InvalidNotificationContentException;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationContent {
    @Column(name = "template_key") // 알림 템플릿 키 (예: "game.invite")
    private String templateKey;
    
    @Convert(converter = JsonConverter.class)
    @Column(name = "variables") // JSON 형식의 변수 (예: { "inviter": "Alice" })
    private Map<String, String> variables;

    private NotificationContent(String templateKey, Map<String, String> variables) {
        validateTemplateKey(templateKey);
        validateVariables(variables);
        this.templateKey = templateKey;
        this.variables = variables;
    }

    public static NotificationContent of(String templateKey, Map<String, String> variables) {
        return new NotificationContent(templateKey, variables);
    }

    // 검증 메서드 수정
    private void validateTemplateKey(String templateKey) {
        if (templateKey == null || templateKey.isBlank()) {
            throw new InvalidNotificationContentException("템플릿 키는 필수입니다.");
        }
        if (templateKey.length() > 50) {
            throw new InvalidNotificationContentException("템플릿 키는 50자를 초과할 수 없습니다.");
        }
    }

    private void validateVariables(Map<String, String> variables) {
        if (variables == null) {
            throw new InvalidNotificationContentException("변수는 null일 수 없습니다.");
        }
    }
}

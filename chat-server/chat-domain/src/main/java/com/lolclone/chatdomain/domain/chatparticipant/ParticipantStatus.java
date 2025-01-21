package com.lolclone.chatdomain.domain.chatparticipant;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantStatus {
    @Enumerated(EnumType.STRING)
    private Status status;
    
    private boolean muted;

    private ParticipantStatus(Status status, boolean muted) {
        this.status = status;
        this.muted = muted;
    }

    public static ParticipantStatus active() {
        return new ParticipantStatus(Status.ACTIVE, false);
    }

    public ParticipantStatus mute() {
        return new ParticipantStatus(this.status, true);
    }

    public ParticipantStatus unmute() {
        return new ParticipantStatus(this.status, false);
    }

    public ParticipantStatus leave() {
        return new ParticipantStatus(Status.LEFT, this.muted);
    }

    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    public boolean isMuted() {
        return this.muted;
    }

    public enum Status {
        ACTIVE, LEFT
    }
}

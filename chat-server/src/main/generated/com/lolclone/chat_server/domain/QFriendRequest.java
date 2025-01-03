package com.lolclone.chat_server.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendRequest is a Querydsl query type for FriendRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendRequest extends EntityPathBase<FriendRequest> {

    private static final long serialVersionUID = -2133633416L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendRequest friendRequest = new QFriendRequest("friendRequest");

    public final com.lolclone.chat_server.common.domain.QBaseTimeEntity _super = new com.lolclone.chat_server.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final QUser receiver;

    public final ComparablePath<java.util.UUID> receiverId = createComparable("receiverId", java.util.UUID.class);

    public final QUser sender;

    public final ComparablePath<java.util.UUID> senderId = createComparable("senderId", java.util.UUID.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QFriendRequest(String variable) {
        this(FriendRequest.class, forVariable(variable), INITS);
    }

    public QFriendRequest(Path<? extends FriendRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendRequest(PathMetadata metadata, PathInits inits) {
        this(FriendRequest.class, metadata, inits);
    }

    public QFriendRequest(Class<? extends FriendRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new QUser(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new QUser(forProperty("sender")) : null;
    }

}


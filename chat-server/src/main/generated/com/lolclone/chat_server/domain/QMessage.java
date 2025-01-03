package com.lolclone.chat_server.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMessage is a Querydsl query type for Message
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessage extends EntityPathBase<Message> {

    private static final long serialVersionUID = -1599898194L;

    public static final QMessage message1 = new QMessage("message1");

    public final com.lolclone.chat_server.common.domain.QBaseTimeEntity _super = new com.lolclone.chat_server.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final BooleanPath deletedByReceiver = createBoolean("deletedByReceiver");

    public final BooleanPath deletedBySender = createBoolean("deletedBySender");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath message = createString("message");

    public final ComparablePath<java.util.UUID> receiverId = createComparable("receiverId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> senderId = createComparable("senderId", java.util.UUID.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QMessage(String variable) {
        super(Message.class, forVariable(variable));
    }

    public QMessage(Path<? extends Message> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMessage(PathMetadata metadata) {
        super(Message.class, metadata);
    }

}


package com.lolclone.chat_server.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendFolder is a Querydsl query type for FriendFolder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendFolder extends EntityPathBase<FriendFolder> {

    private static final long serialVersionUID = -264759451L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendFolder friendFolder = new QFriendFolder("friendFolder");

    public final com.lolclone.chat_server.common.domain.QBaseTimeEntity _super = new com.lolclone.chat_server.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final SetPath<FolderFriend, QFolderFriend> friends = this.<FolderFriend, QFolderFriend>createSet("friends", FolderFriend.class, QFolderFriend.class, PathInits.DIRECT2);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public final QUser user;

    public QFriendFolder(String variable) {
        this(FriendFolder.class, forVariable(variable), INITS);
    }

    public QFriendFolder(Path<? extends FriendFolder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendFolder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendFolder(PathMetadata metadata, PathInits inits) {
        this(FriendFolder.class, metadata, inits);
    }

    public QFriendFolder(Class<? extends FriendFolder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}


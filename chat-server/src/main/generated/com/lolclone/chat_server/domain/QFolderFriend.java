package com.lolclone.chat_server.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFolderFriend is a Querydsl query type for FolderFriend
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFolderFriend extends EntityPathBase<FolderFriend> {

    private static final long serialVersionUID = -1208517787L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFolderFriend folderFriend = new QFolderFriend("folderFriend");

    public final com.lolclone.chat_server.common.domain.QBaseTimeEntity _super = new com.lolclone.chat_server.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QFriendFolder folder;

    public final QFriend friend;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final NumberPath<Integer> orderIndex = createNumber("orderIndex", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QFolderFriend(String variable) {
        this(FolderFriend.class, forVariable(variable), INITS);
    }

    public QFolderFriend(Path<? extends FolderFriend> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFolderFriend(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFolderFriend(PathMetadata metadata, PathInits inits) {
        this(FolderFriend.class, metadata, inits);
    }

    public QFolderFriend(Class<? extends FolderFriend> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.folder = inits.isInitialized("folder") ? new QFriendFolder(forProperty("folder"), inits.get("folder")) : null;
        this.friend = inits.isInitialized("friend") ? new QFriend(forProperty("friend"), inits.get("friend")) : null;
    }

}


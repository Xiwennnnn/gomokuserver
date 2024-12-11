create table if not exists users
(
    id          bigint primary key,
    username    varchar(20) not null unique,
    password    varchar(20) not null,
    handle      varchar(20) not null,
    role        varchar(20) not null,
    win_count   int default 0,
    total_count int default 0,
    avatar_url  varchar(300),
    status      int default 0
);

create table if not exists games
(
    id              bigint primary key,
    black_player_id bigint       not null,
    white_player_id bigint       not null,
    game_status     varchar(300) not null,
    move_count      int          not null,
    winner_id       bigint,
    game_format     int          not null,
    start_time      datetime     not null,
    end_time        datetime,
    is_active       boolean
);

create table if not exists rooms
(
    id                 bigint primary key,
    game_id            bigint,
    owner_id           bigint,
    player_id          bigint,
    player_count       int      not null,
    create_time        datetime not null,
    status             int     default 0,
    is_private         boolean  not null
);

create index if not exists index_owner_id on rooms(owner_id);
create index if not exists index_player_id on rooms(player_id);

create table if not exists game_moves
(
    id            bigint primary key auto_increment,
    game_id       bigint   not null,
    player_id     bigint   not null,
    move_number   int      not null,
    move_time     datetime not null,
    move_position int      not null,
    is_undo       boolean default false,
    foreign key (game_id) references games (id)
);

create index if not exists index_player_id_move_number on game_moves(player_id, move_number);

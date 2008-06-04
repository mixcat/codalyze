create table javancss_imports(
    id IDENTITY,
    date date,
    metadata varchar(50)
);

create table javancss_packages(
    id IDENTITY,
    import_id int not null,
    name varchar(1024) not null,
    ncss int not null,
    classes int not null,
    functions int not null,
    javadocs int not null,
    javadoc_lines int not null,
    single_comment_lines int not null,
    multi_comment_lines int not null
);

create table javancss_objects (
    id IDENTITY,
    import_id int not null,
    name varchar(1024) not null,
    ncss int not null,
    classes int not null,
    functions int not null,
    javadocs int not null
);

create table javancss_functions  (
    id IDENTITY,
    import_id int not null,
    name varchar(1024) not null,
    ncss int not null,
    ccn int not null,
    javadocs int not null
);
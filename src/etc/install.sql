spool install.log

prompt
prompt Creating table NODES
prompt ====================
prompt
create table NODES
(
  PK     NUMBER not null,
  PARENT NUMBER,
  NAME   VARCHAR2(80)
)
;
alter table NODES
  add constraint PK_KEYS primary key (PK);
alter table NODES
  add constraint FK_KEYS_1 foreign key (PARENT)
  references NODES (PK);
create index I_KEYS_1 on NODES (PARENT);

prompt
prompt Creating table PREF_VALUE
prompt =========================
prompt
create table PREF_VALUE
(
  PK     NUMBER not null,
  PARENT NUMBER,
  KEY    VARCHAR2(80),
  VALUE  VARCHAR2(4000)
)
;
alter table PREF_VALUE
  add constraint PK_PREF_VALUE primary key (PK);
alter table PREF_VALUE
  add constraint FK_PREF_VALUE foreign key (PARENT)
  references NODES (PK) on delete cascade;
create index I_PREF_VALUE_1 on PREF_VALUE (PARENT);

prompt
prompt Creating sequence S_NODES
prompt =========================
prompt
create sequence S_NODES;

prompt
prompt Creating sequence S_PREF_VALUE
prompt ==============================
prompt
create sequence S_PREF_VALUE;

insert into NODES Values (1, NULL, NULL);
insert into NODES Values (2, 1, 'rootNode');

spool off

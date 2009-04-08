-- Autogenerated on Thu Apr  2 12:46:10 2009 by mkaudit.pl

create table audit.feature_cvterm_dbxref (
    dbxref_id integer not null
  , feature_cvterm_id integer not null
  , feature_cvterm_dbxref_id integer not null
) inherits (audit.audit);

create or replace function audit.audit_feature_cvterm_dbxref_insert_proc()
returns trigger
as $$
BEGIN
  raise exception 'Cannot insert directly into audit.feature_cvterm_dbxref. Use one of the child tables.';
END;
$$ language plpgsql;
create trigger feature_cvterm_dbxref_insert_tr before insert on audit.feature_cvterm_dbxref
    for each statement execute procedure audit.audit_feature_cvterm_dbxref_insert_proc();
grant select on audit.feature_cvterm_dbxref to chado_ro_role;
grant select, insert on audit.feature_cvterm_dbxref to chado_rw_role;
grant execute on function audit.audit_feature_cvterm_dbxref_insert_proc() to chado_rw_role;


create table audit.feature_cvterm_dbxref_insert (
    constraint feature_cvterm_dbxref_insert_ck check (type = 'INSERT')
) inherits (audit.feature_cvterm_dbxref);
alter table audit.feature_cvterm_dbxref_insert alter type set default 'INSERT';
grant select on audit.feature_cvterm_dbxref_insert to chado_ro_role;
grant select, insert on audit.feature_cvterm_dbxref_insert to chado_rw_role;

create or replace function audit.public_feature_cvterm_dbxref_insert_proc()
returns trigger
as $$
BEGIN
  insert into audit.feature_cvterm_dbxref_insert (
      feature_cvterm_dbxref_id, feature_cvterm_id, dbxref_id
  ) values (
      new.feature_cvterm_dbxref_id, new.feature_cvterm_id, new.dbxref_id
  );
  return new;
END;
$$ language plpgsql;
create trigger feature_cvterm_dbxref_audit_insert_tr after insert on public.feature_cvterm_dbxref
    for each row execute procedure audit.public_feature_cvterm_dbxref_insert_proc();
grant execute on function audit.public_feature_cvterm_dbxref_insert_proc() to chado_rw_role;


create table audit.feature_cvterm_dbxref_update (
    constraint feature_cvterm_dbxref_update_ck check (type = 'UPDATE')
  , old_feature_cvterm_id integer not null
  , old_dbxref_id integer not null
) inherits (audit.feature_cvterm_dbxref);
alter table audit.feature_cvterm_dbxref_update alter type set default 'UPDATE';
grant select on audit.feature_cvterm_dbxref_update to chado_ro_role;
grant select, insert on audit.feature_cvterm_dbxref_update to chado_rw_role;

create or replace function audit.public_feature_cvterm_dbxref_update_proc()
returns trigger
as $$
BEGIN
  if old.feature_cvterm_dbxref_id <> new.feature_cvterm_dbxref_id or old.feature_cvterm_dbxref_id is null <> new.feature_cvterm_dbxref_id is null then
    raise exception 'If you want to change feature_cvterm_dbxref.feature_cvterm_dbxref_id (do you really?) then disable the audit trigger feature_cvterm_dbxref_audit_update_tr';
  end if;
  insert into audit.feature_cvterm_dbxref_update (
      feature_cvterm_dbxref_id, feature_cvterm_id, dbxref_id,
      old_feature_cvterm_id, old_dbxref_id
   ) values (
       new.feature_cvterm_dbxref_id, new.feature_cvterm_id, new.dbxref_id,
       old.feature_cvterm_id, old.dbxref_id
   );
  return new;
END;
$$ language plpgsql;
create trigger feature_cvterm_dbxref_audit_update_tr after update on public.feature_cvterm_dbxref
    for each row execute procedure audit.public_feature_cvterm_dbxref_update_proc();
grant execute on function audit.public_feature_cvterm_dbxref_update_proc() to chado_rw_role;


create table audit.feature_cvterm_dbxref_delete (
    constraint feature_cvterm_dbxref_delete_ck check (type = 'DELETE')
) inherits (audit.feature_cvterm_dbxref);
alter table audit.feature_cvterm_dbxref_delete alter type set default 'DELETE';
grant select on audit.feature_cvterm_dbxref_delete to chado_ro_role;
grant select, insert on audit.feature_cvterm_dbxref_delete to chado_rw_role;

create or replace function audit.public_feature_cvterm_dbxref_delete_proc()
returns trigger
as $$
BEGIN
  insert into audit.feature_cvterm_dbxref_delete (
      feature_cvterm_dbxref_id, feature_cvterm_id, dbxref_id
  ) values (
      old.feature_cvterm_dbxref_id, old.feature_cvterm_id, old.dbxref_id
  );
  return old;
END;
$$ language plpgsql;
create trigger feature_cvterm_dbxref_audit_delete_tr after delete on public.feature_cvterm_dbxref
    for each row execute procedure audit.public_feature_cvterm_dbxref_delete_proc();
grant execute on function audit.public_feature_cvterm_dbxref_delete_proc() to chado_rw_role;
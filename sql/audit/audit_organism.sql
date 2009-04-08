-- Autogenerated on Thu Apr  2 12:46:11 2009 by mkaudit.pl

create table audit.organism (
    common_name character varying(255)
  , comment text
  , genus character varying(255) not null
  , organism_id integer not null
  , species character varying(255) not null
  , abbreviation character varying(255)
) inherits (audit.audit);

create or replace function audit.audit_organism_insert_proc()
returns trigger
as $$
BEGIN
  raise exception 'Cannot insert directly into audit.organism. Use one of the child tables.';
END;
$$ language plpgsql;
create trigger organism_insert_tr before insert on audit.organism
    for each statement execute procedure audit.audit_organism_insert_proc();
grant select on audit.organism to chado_ro_role;
grant select, insert on audit.organism to chado_rw_role;
grant execute on function audit.audit_organism_insert_proc() to chado_rw_role;


create table audit.organism_insert (
    constraint organism_insert_ck check (type = 'INSERT')
) inherits (audit.organism);
alter table audit.organism_insert alter type set default 'INSERT';
grant select on audit.organism_insert to chado_ro_role;
grant select, insert on audit.organism_insert to chado_rw_role;

create or replace function audit.public_organism_insert_proc()
returns trigger
as $$
BEGIN
  insert into audit.organism_insert (
      organism_id, abbreviation, genus, species, common_name, comment
  ) values (
      new.organism_id, new.abbreviation, new.genus, new.species, new.common_name, new.comment
  );
  return new;
END;
$$ language plpgsql;
create trigger organism_audit_insert_tr after insert on public.organism
    for each row execute procedure audit.public_organism_insert_proc();
grant execute on function audit.public_organism_insert_proc() to chado_rw_role;


create table audit.organism_update (
    constraint organism_update_ck check (type = 'UPDATE')
  , old_species character varying(255) not null
  , old_genus character varying(255) not null
  , old_common_name character varying(255)
  , old_abbreviation character varying(255)
  , old_comment text
) inherits (audit.organism);
alter table audit.organism_update alter type set default 'UPDATE';
grant select on audit.organism_update to chado_ro_role;
grant select, insert on audit.organism_update to chado_rw_role;

create or replace function audit.public_organism_update_proc()
returns trigger
as $$
BEGIN
  if old.organism_id <> new.organism_id or old.organism_id is null <> new.organism_id is null then
    raise exception 'If you want to change organism.organism_id (do you really?) then disable the audit trigger organism_audit_update_tr';
  end if;
  insert into audit.organism_update (
      organism_id, abbreviation, genus, species, common_name, comment,
      old_abbreviation, old_genus, old_species, old_common_name, old_comment
   ) values (
       new.organism_id, new.abbreviation, new.genus, new.species, new.common_name, new.comment,
       old.abbreviation, old.genus, old.species, old.common_name, old.comment
   );
  return new;
END;
$$ language plpgsql;
create trigger organism_audit_update_tr after update on public.organism
    for each row execute procedure audit.public_organism_update_proc();
grant execute on function audit.public_organism_update_proc() to chado_rw_role;


create table audit.organism_delete (
    constraint organism_delete_ck check (type = 'DELETE')
) inherits (audit.organism);
alter table audit.organism_delete alter type set default 'DELETE';
grant select on audit.organism_delete to chado_ro_role;
grant select, insert on audit.organism_delete to chado_rw_role;

create or replace function audit.public_organism_delete_proc()
returns trigger
as $$
BEGIN
  insert into audit.organism_delete (
      organism_id, abbreviation, genus, species, common_name, comment
  ) values (
      old.organism_id, old.abbreviation, old.genus, old.species, old.common_name, old.comment
  );
  return old;
END;
$$ language plpgsql;
create trigger organism_audit_delete_tr after delete on public.organism
    for each row execute procedure audit.public_organism_delete_proc();
grant execute on function audit.public_organism_delete_proc() to chado_rw_role;
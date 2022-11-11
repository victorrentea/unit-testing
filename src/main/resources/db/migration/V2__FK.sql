
alter table product add constraint FK_PRODUCT_SUPPLIER foreign key (supplier_id) references supplier;

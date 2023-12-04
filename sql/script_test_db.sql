use example_person;

select * from `person`;

select * from `person_has_friend`;

select * from `person_has_friend` where `person_id` = 1;

select * from `person_has_friend` where `person_id` = 3 and `friend_id` = 4;

delete from `person_has_friend` where `person_id` = 3 and `friend_id` = 4;

update `person_has_friend` set `person_id` = 1, `friend_id` = 4 where `person_id` = 1 and `friend_id` = 4; 

insert into `person_has_friend`(`person_id`, `friend_id`) values(3, 4);

update `person_has_friend` set `person_id` = 4, `friend_id` = 3 where `person_id` = 3 and `friend_id` = 4;

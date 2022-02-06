select a.id, sum(t.amount) from account a
join transfer t on (t.source_id = a.id)
where t.transfer_time >= '2019-01-01'::date
group by a.id
having sum(t.amount) > 1000;
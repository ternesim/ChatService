SELECT r.id as r_id, r.name as r_name FROM rooms r
    JOIN messages m ON m.room_id = r.id;
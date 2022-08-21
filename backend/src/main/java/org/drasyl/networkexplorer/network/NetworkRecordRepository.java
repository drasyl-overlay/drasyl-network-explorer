package org.drasyl.networkexplorer.network;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkRecordRepository extends MongoRepository<NetworkRecord, String> {
    NetworkRecord findFirstByOrderByCreatedDateDesc();
}

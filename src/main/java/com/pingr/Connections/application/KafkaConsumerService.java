package com.pingr.Connections.application;

import com.pingr.Connections.core.Account;
import com.pingr.Connections.core.services.SynchronizeAccount;
import com.pingr.Connections.core.events.AccountCreatedEvent;
import com.pingr.Connections.core.events.AccountDeletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private SynchronizeAccount syncAcc;

    @KafkaListener(
            containerFactory = "accountCreatedEventKafkaListenerContainerFactory",
            topics = "${topics.acc_created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )

    public void handleAccountCreation(AccountCreatedEvent event) {
        Account account = event.extract();
        this.syncAcc.store(account);
    }

    @KafkaListener(
            containerFactory = "accountDeletedEventKafkaListenerContainerFactory",
            topics = "${topics.acc_deleted}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleAccountDeletion(AccountDeletedEvent event) {
        Account account = event.extract();
        this.syncAcc.remove(account);
    }

    @KafkaListener(
            containerFactory = "accountDeletedEventKafkaListenerContainerFactory",
            topics = "${topics.acc_deleted}",
            groupId = "${spring.kafka.consumer.group-id}"
    )

    public void handleAccountRemoval(AccountDeletedEvent event) {
        Account account = event.extract();
        System.out.println(account);
    }
}

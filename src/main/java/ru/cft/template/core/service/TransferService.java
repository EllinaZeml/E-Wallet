package ru.cft.template.core.service;


import ru.cft.template.core.entity.TransferEntity;


import java.util.List;

public interface TransferService {
    TransferEntity getTransferInfo(Long transferId);
    List<TransferEntity> getFilteredTransfers(Long senderId, Long receiverId, String transferType);
    TransferEntity transferByPhoneReceiver(Long senderId, String phoneNumber, int amount);
    TransferEntity transferByWalletReceiver(Long senderId, int walletNumber, int amount);

}

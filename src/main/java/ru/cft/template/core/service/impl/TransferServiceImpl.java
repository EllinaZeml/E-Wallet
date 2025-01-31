package ru.cft.template.core.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.template.core.entity.TransferEntity;
import ru.cft.template.core.entity.UserEntity;
import ru.cft.template.core.entity.WalletEntity;
import ru.cft.template.core.repository.TransferRepository;
import ru.cft.template.core.repository.UserRepozitory;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.TransferService;
import ru.cft.template.core.service.status.TransferStatus;
import ru.cft.template.exception.TransferException;

import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService {


    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserRepozitory userRepozitory;
    @Autowired
    private WalletRepository walletRepository;


    @Override
    public TransferEntity getTransferInfo(Long transferId) {
        Optional<TransferEntity> transferOpt = transferRepository.findById(transferId);
        return transferOpt.orElse(null);
    }

    @Override

    public List<TransferEntity> getFilteredTransfers(Long senderId, Long receiverId, String transferType) {
        // Если заданы и отправитель, и получатель
        if (senderId != null && receiverId != null) {
            return transferRepository.findBySenderAndReceiver(senderId, receiverId);
        }

        // Если задан только отправитель
        if (senderId != null) {
            if (transferType != null) {

                if ("incoming".equals(transferType)) {
                    return transferRepository.findByReceiver(senderId);
                } else if ("outgoing".equals(transferType)) {
                    return transferRepository.findBySender(senderId);
                }
            } else {
                return transferRepository.findBySender(senderId);
            }
        }

        // Если задан только получатель
        if (receiverId != null) {
            if (transferType != null) {

                if ("incoming".equals(transferType)) {
                    return transferRepository.findByReceiver(receiverId);
                } else if ("outgoing".equals(transferType)) {
                    return transferRepository.findBySender(receiverId);
                }
            } else {
                return transferRepository.findByReceiver(receiverId);
            }
        }

        // Если не заданы фильтры, возвращаем пустой список
        return List.of();
    }


    @Override
    @Transactional
    public TransferEntity transferByPhoneReceiver(Long senderId, String phoneNumber, int amount) {

        Optional<WalletEntity> senderWalletOpt = walletRepository.findByUserId(senderId);
        if (!senderWalletOpt.isPresent()) {
            throw new TransferException("Пользователь не найден");
        }
        WalletEntity senderWallet = senderWalletOpt.get(); // Кошелек отправителя


        Optional<UserEntity> receiverUserOpt = userRepozitory.findByPhoneNumber(phoneNumber);
        if (!receiverUserOpt.isPresent()) {
            throw new TransferException( "Пользователь с таким номером телефона не найден");
        }
        UserEntity receiverUser = receiverUserOpt.get(); // Получатель


        Optional<WalletEntity> receiverWalletOpt = walletRepository.findByUserId(receiverUser.getId());
        if (!receiverWalletOpt.isPresent()) {
            throw new TransferException( "Кошелек получателя не найден");
        }
        WalletEntity receiverWallet = receiverWalletOpt.get();

        if(senderWallet.getId().equals(receiverWallet.getId())){
            throw new TransferException("Вы не можете перевести деньги самому себе через один и тот же кошелек");
        }


        if (senderWallet.getBalance() < amount) {
            throw new TransferException("На счете недостаточно средств");
        }

        TransferEntity transfer = new TransferEntity();
        transfer.setStatus(TransferStatus.PENDING);

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        transfer.setStatus(TransferStatus.COMPLETED);
        transferRepository.save(transfer);

        return transfer;
    }

    @Override
    @Transactional
    public TransferEntity transferByWalletReceiver(Long senderId, int walletNumber, int amount) {

        Optional<WalletEntity> senderWalletOpt = walletRepository.findByUserId(senderId);
        if (!senderWalletOpt.isPresent()) {
            throw new TransferException("Отправитель не найден");
        }
        WalletEntity senderWallet = senderWalletOpt.get();

        Optional<WalletEntity> receiverWalletOpt = walletRepository.findByWalletNumber(walletNumber);
        if (!receiverWalletOpt.isPresent()) {
            throw new TransferException("Кошелек получателя не найден");
        }
        WalletEntity receiverWallet = receiverWalletOpt.get(); // Кошелек получателя

        if (senderWallet.getId().equals(receiverWallet.getId())) {
            throw new TransferException("Вы не можете перевести деньги самому себе через один и тот же кошелек");
        }

        if (senderWallet.getBalance() < amount) {
            throw new TransferException("На счете недостаточно средств");
        }

        TransferEntity transfer = new TransferEntity();
        transfer.setStatus(TransferStatus.PENDING);

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        transfer.setStatus(TransferStatus.COMPLETED);
        transferRepository.save(transfer);

        return transfer;
    }
}

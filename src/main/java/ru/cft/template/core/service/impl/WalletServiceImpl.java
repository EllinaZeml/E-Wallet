package ru.cft.template.core.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.cft.template.core.entity.WalletEntity;
import ru.cft.template.core.repository.UserRepozitory;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.WalletService;
import java.util.Optional;
import java.util.Random;


@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepozitory userRepository;

    public WalletServiceImpl(WalletRepository walletRepository, UserRepozitory userRepozitory) {
        this.walletRepository = walletRepository;
        this.userRepository=userRepozitory;
    }
    @Override
    public WalletEntity getWallet(Long userId) {
        Optional<WalletEntity> optionalWallet = walletRepository.findByUserId(userId);
        if(optionalWallet.isPresent()){
            return optionalWallet.get();
        }
        throw new RuntimeException("Кошелек не найден для данного пользователя");

    }
    // Функционал рулетки HESOYAM (с вероятностью 25% пользователь получает 10 д.е.)
    @Override
    @Transactional
    public WalletEntity performHESOYAM(Long userId) {
        Optional<WalletEntity> walletOptional = walletRepository.findByUserId(userId);
        if(walletOptional.isPresent()){
            WalletEntity wallet = walletOptional.get();

            //25% шанс
            Random random = new Random();
            int chance = random.nextInt(4);
            if(chance==0){
                wallet.setBalance(wallet.getBalance()+10);
            }
            return walletRepository.save(wallet);

        }
        throw new RuntimeException("Кошелек не найден для данного пользователя");
    }
}

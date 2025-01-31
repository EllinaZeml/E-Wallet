package ru.cft.template.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.cft.template.api.dto.TransferDTO;
import ru.cft.template.core.entity.TransferEntity;
import ru.cft.template.core.service.TransferService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    @Autowired
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    //получение информации о переводе
    @GetMapping("/{transferId}")
    public TransferDTO getTransferInfo(@PathVariable Long transferId) {
        TransferEntity transfer = transferService.getTransferInfo(transferId);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Перевод не найден с ID: " + transferId);
        }
        return new TransferDTO( transfer.getAmount(), transfer.getType());
    }
    @PostMapping("/transferByPhone")
    public TransferDTO transferByPhoneReceiver(
            @RequestParam  Long senderId, // либо  @RequestParam
            @RequestParam String phoneNumber,
            @RequestParam int amount) {
        TransferEntity transfer =transferService.transferByPhoneReceiver(senderId, phoneNumber, amount);
        return new TransferDTO(transfer.getAmount(), transfer.getType());
    }

    @PostMapping("/transferByWallet")
    public TransferDTO transferByWalletReceiver(
            @RequestParam  Long senderId,
            @RequestParam int walletNumber,
            @RequestParam int amount) {
        TransferEntity transfer =  transferService.transferByWalletReceiver(senderId, walletNumber, amount);
        return new TransferDTO(transfer.getAmount(), transfer.getType());
    }

    @GetMapping
    public List<TransferDTO> getFilteredTransfers(
            @RequestParam(required = false) Long senderId,
            @RequestParam(required = false) Long receiverId,
            @RequestParam(required = false) String transferType) {
        List<TransferEntity> transfers = transferService.getFilteredTransfers(senderId, receiverId, transferType);
        List<TransferDTO> transferDTO = new ArrayList<>();
        for(TransferEntity transfer : transfers){
            transferDTO.add(new TransferDTO(
                    transfer.getAmount(),
                    transfer.getType()
            ));
        }
        return transferDTO;
    }
}

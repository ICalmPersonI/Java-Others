package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@RestController
@Transactional
public class ScheduledTasks {

    @Autowired
    private PrivateCodeRepository privateCodeRepository;

    @Scheduled(fixedRate = 500)
    public void checkTime() {
        List<PrivateCode> privateCodeList = new ArrayList<>();
        privateCodeRepository.findAll().forEach(privateCodeList::add);
        if (!privateCodeList.isEmpty()) {
            for (PrivateCode code : privateCodeList) {
                if (code.getTime() < 0) {
                    if (privateCodeRepository.deleteByUuid(code.getUuid()).isPresent()) {
                        System.out.printf("%s deleted by time.\n", code.getUuid());
                    }
                }
                if ((code.getViews() == 0 && code.getTime() == 0) || code.getViews() < 0) {
                    if (privateCodeRepository.deleteByUuid(code.getUuid()).isPresent()) {
                        System.out.printf("%s deleted by views.\n", code.getUuid());
                    }
                }
            }
        }
    }


    @Scheduled(fixedDelay = 1000)
    public void updateTime() {
        List<PrivateCode> privateCodeList = new ArrayList<>();
        privateCodeRepository.findAll().forEach(privateCodeList::add);
        if (!privateCodeList.isEmpty()) {
            for (PrivateCode code : privateCodeList) {
                if (code.getTime() > 0) {
                    privateCodeRepository.updateTime(code.getUuid(), code.getTime() - 1);
                }
            }
        }
    }




    /*
    @Transactional
    private void delete(PrivateCode code) {
        if (privateCodeRepository.deleteByUuid(code.getUuid()).isPresent()) {
            System.out.printf("%s deleted.\n", code.getUuid());
        }
    }

     */

}

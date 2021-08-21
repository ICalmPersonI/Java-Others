package platform;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivateCodeRepository extends CrudRepository<PrivateCode, Long> {
    Optional<PrivateCode> findByUuid(String uuid);
    Optional<PrivateCode> deleteByUuid(String uuid);

    @Modifying
    @Query("update PrivateCode u set u.views = :views where u.uuid = :uuid")
    public void updateViews(@Param(value = "uuid") String uuid, @Param(value = "views") int views);

    @Modifying
    @Query("update PrivateCode u set u.time = :time where u.uuid = :uuid")
    public void updateTime(@Param(value = "uuid") String uuid, @Param(value = "time") int time);
}

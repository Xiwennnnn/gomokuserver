package whpu.gomoku.gomokuserver.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import whpu.gomoku.gomokuserver.data.entity.Game;

@Repository
@Mapper
public interface GameMapper extends BaseMapper<Game> {
}
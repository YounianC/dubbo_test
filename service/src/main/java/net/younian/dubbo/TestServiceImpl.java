package net.younian.dubbo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/11.
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private IStrategyDao iStrategyDao;

    public String getData() {
        List<Map<Object, Object>> list = iStrategyDao.getAll();
        return JSONObject.toJSONString(list);
    }
}

package org.turtlex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by coolkids on 7/10/2016.
 */
@Slf4j
@Service
@Transactional
class EnvironmentServiceImpl implements EnvironmentService {
    @Inject
    private Environment environment;

    @Override
    public void dump() {
        final MutablePropertySources sources = AbstractEnvironment.class.cast(environment).getPropertySources();
        Iterator iterator = sources.iterator();
        while (iterator.hasNext()) {
            MapPropertySource mapPropertySource = MapPropertySource.class.cast(iterator.next());
            Arrays.asList(mapPropertySource.getPropertyNames())
                    .forEach(x -> log.debug("property key: {}, property value: {}", x, mapPropertySource.getProperty(x)));
        }
    }
}

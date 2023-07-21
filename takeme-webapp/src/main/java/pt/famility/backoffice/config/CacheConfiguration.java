package pt.famility.backoffice.config;

import java.time.Duration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(pt.famility.backoffice.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Tutor.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Tutor.class.getName() + ".locations", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Tutor.class.getName() + ".children", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Child.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Child.class.getName() + ".tutors", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Child.class.getName() + ".organizations", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Organization.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Organization.class.getName() + ".locations", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Organization.class.getName() + ".contacts", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Contact.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Vehicle.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Driver.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.ChildSubscription.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Location.class.getName() + ".tutors", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Location.class.getName() + ".organizations", jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.StopPoint.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.StopAuditEvent.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Itinerary.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.Service.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.ServiceStopPoint.class.getName(), jcacheConfiguration);
            cm.createCache(
                pt.famility.backoffice.domain.ServiceStopPoint.class.getName() + ".serviceStopPointDaysOfWeeks",
                jcacheConfiguration
            );
            cm.createCache(pt.famility.backoffice.domain.ServiceStopPointFrequency.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.IdentificationCardType.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.UserMessagingToken.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.NotificationChannel.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.NotificationChannelUser.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.UserNotification.class.getName(), jcacheConfiguration);
            cm.createCache(pt.famility.backoffice.domain.NotificationType.class.getName(), jcacheConfiguration);
            createCache(cm, pt.famility.backoffice.domain.Location.class.getName());
            createCache(cm, pt.famility.backoffice.domain.PromoterService.class.getName());
            createCache(cm, pt.famility.backoffice.domain.PromoterItinerary.class.getName());
            createCache(cm, pt.famility.backoffice.domain.PromoterStopPoint.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Tutor.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Tutor.class.getName() + ".locations");
            createCache(cm, pt.famility.backoffice.domain.Tutor.class.getName() + ".children");
            createCache(cm, pt.famility.backoffice.domain.Child.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Child.class.getName() + ".tutors");
            createCache(cm, pt.famility.backoffice.domain.Child.class.getName() + ".organizations");
            createCache(cm, pt.famility.backoffice.domain.Organization.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Organization.class.getName() + ".locations");
            createCache(cm, pt.famility.backoffice.domain.Organization.class.getName() + ".children");
            createCache(cm, pt.famility.backoffice.domain.Contact.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Vehicle.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Driver.class.getName());
            createCache(cm, pt.famility.backoffice.domain.ChildSubscription.class.getName());
            createCache(cm, pt.famility.backoffice.domain.StopPoint.class.getName());
            createCache(cm, pt.famility.backoffice.domain.StopAuditEvent.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Itinerary.class.getName());
            createCache(cm, pt.famility.backoffice.domain.Service.class.getName());
            createCache(cm, pt.famility.backoffice.domain.ServiceStopPoint.class.getName());
            createCache(cm, pt.famility.backoffice.domain.ServiceStopPoint.class.getName() + ".serviceStopPointDaysOfWeeks");
            createCache(cm, pt.famility.backoffice.domain.ServiceStopPointFrequency.class.getName());
            createCache(cm, pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek.class.getName());
            createCache(cm, pt.famility.backoffice.domain.ServiceStopPointDaysOfWeek.class.getName() + ".serviceStopPoints");
            createCache(cm, pt.famility.backoffice.domain.IdentificationCardType.class.getName());
            createCache(cm, pt.famility.backoffice.domain.UserMessagingToken.class.getName());
            createCache(cm, pt.famility.backoffice.domain.NotificationChannel.class.getName());
            createCache(cm, pt.famility.backoffice.domain.NotificationChannelUser.class.getName());
            createCache(cm, pt.famility.backoffice.domain.UserNotification.class.getName());
            createCache(cm, pt.famility.backoffice.domain.NotificationType.class.getName());
            createCache(cm, pt.famility.backoffice.domain.ChildItinerarySubscription.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}

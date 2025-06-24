package com.workout.tracker.aspect;

import com.workout.tracker.config.RateLimiterService;
import com.workout.tracker.exception.TooManyRequestsException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimiterService limiter;

    @Pointcut("execution(* com.workout.tracker.service.impl.WorkoutScheduleServiceImpl.createWorkoutSchedule(..))")
    public void createSchedulePointcut() {}

    @Pointcut("@annotation(com.workout.tracker.annotations.RateLimited)")
    public void rateLimitedEndpoints() {}


    @Before("createSchedulePointcut()")
    public void checkRateLimit(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String username = (String) args[0]; // Assumes first param is username

        var bucket = limiter.resolveBucket(username);
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestsException("Too many requests. Please try again later.");
        }
    }
}


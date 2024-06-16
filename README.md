# projet-2024-kool-krazy-klub

[![Statut du workflow](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions/workflows/tools_installation.yaml/badge.svg)](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions)
[![Statut du workflow 2](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions/workflows/docker_push.yaml/badge.svg)](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions)
[![Quality Gate Status](https://sonar.deed1t.master-sdl.ovh/api/project_badges/measure?project=projet-2024-kool-krazy-klub&metric=alert_status&token=sqb_247c2a85db8b2408f987cb153f5a5149e553c8fd)](https://sonar.deed1t.master-sdl.ovh/dashboard?id=projet-2024-kool-krazy-klub)

## Webservice usage with curl command

TODO

## Prometheus Query

To measure the number of requests per second when a **task** is created, you can use the following Prometheus query:
```prometheus
rate(add_tache_bdd_responsetime_seconds_count[10m])
```
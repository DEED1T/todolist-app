# projet-2024-kool-krazy-klub

[![Statut du workflow](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions/workflows/tools_installation.yaml/badge.svg)](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions)
[![Statut du workflow 2](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions/workflows/docker_push.yaml/badge.svg)](https://github.com/M2DL-IVVQ-DevOps/projet-2024-kool-krazy-klub/actions)
[![Quality Gate Status](https://sonar.deed1t.master-sdl.ovh/api/project_badges/measure?project=projet-2024-kool-krazy-klub&metric=alert_status&token=sqb_247c2a85db8b2408f987cb153f5a5149e553c8fd)](https://sonar.deed1t.master-sdl.ovh/dashboard?id=projet-2024-kool-krazy-klub)

## Webservice usage with curl command

| Endpoint                | Type   | Attributes                                    | Expected Response                                  | Description                                          |
|-------------------------|--------|-----------------------------------------------|---------------------------------------------------|------------------------------------------------------|
| `/api/todos`            | GET    | None                                          | JSON array of all tasks                           | Retrieve all tasks                                   |
| `/api/todo`             | GET    | `id` (long)                                   | JSON object of the task with the specified ID     | Retrieve a task by its ID                            |
| `/api/addTodo`          | POST   | `titre` (string), `texte` (string)            | JSON object of the created task                   | Create a new task with a title and text              |
| `/api/deleteTodo`       | DELETE | `id` (long)                                   | None                                              | Delete a task by its ID                              |
| `/api/updateTodo`       | PATCH  | `id` (long), `titre` (string, optional), `texte` (string, optional) | JSON object of the updated task or error message  | Update a task's title and/or text by its ID          |

Can be used with these domain names : 
- `app.notdelirious.master-sdl.ovh`
- `app.topin060.master-sdl.ovh`

### Examples

Get all tasks :
```shell
curl -X GET "http://app.notdelirious.master-sdl.ovh/api/todos" -H "Accept: application/json"
```

Get a task by ID:
```shell
curl -X GET "http://app.notdelirious.master-sdl.ovh/api/todo?id=<task-id>" -H "Accept: application/json"
```

Add a new task:
```shell
curl -X POST "http://app.notdelirious.master-sdl.ovh/api/addTodo?titre=<task-title>&texte=<task-text>" -H "Accept: application/json"
```

Delete a task by ID:
```shell
curl -X DELETE "http://app.notdelirious.master-sdl.ovh/api/deleteTodo?id=<task-id>"
```

Update a task by ID:
```shell
curl -X PATCH "http://app.notdelirious.master-sdl.ovh/api/updateTodo?id=<task-id>&titre=<new-title>&texte=<new-text>" -H "Accept: application/json"
```
## Prometheus Query

To measure the number of requests per second when a **task** is created, you can use the following Prometheus query:
```prometheus
rate(add_tache_bdd_responsetime_seconds_count[10m])
```
# Subscription service

## Architecture

Application follow a microservices approach. Modules are:
- Subscription service: Main service. Allow to create new subscription and generate a mail to final user
- Mail service: Listen to request of mailing users
- Public service: Act as proxy server to Subscription service

As you can see, in Subscription service are more methods implemented than only public subscription needs.
The main intention is to allow internal applications to connect to this service to request some data about
subscriptions.

This services, stores in a mongo collection all subscriptions received. To simplify this scenario, mongo only
have 1 instance (in a real production environment, we use a balanced replica set)

As you can see, this service is loose coupled of both database and mail system, using Ports and adapters patter.
It will be easy to change both of them to another implementations in case of needed.

To prevent access to this services, I've created a Public service, following proxy pattern. This allow me to
expose only the methods I want.

Communication between Subscription service and Mail service is done by a Kafka topic. This allow Subscription
service to forgot about the process of sending mail, and only have to insert a message in the topic.

This also prevent failures in subscription service by cause of mail service missfunction.

Features that I think that can be interesting:
- Mailing template: Subscription service always send the same mail. It would be interesting to allow some kind
of templates.
- Mailing in background: It would interesting to process mailing in a different task that the main one,
using database for it and a scheduled task.
- Load balance: To get a high availability, services should be balanced with a haproxy or better, in a cluster
configuration.

Libraries:
- spring boot and spring mvc
- lombok: remove a lot of boilerplate code
- springfox: to publish swagger definitions and swagger ui
- apache-curator: to allow us to test kafka without a real kafka broker
- mockito: unit testing
- kafka: event based communication between subscription service and mail service
- mongo: DB for subcription service

## Build & install

In the main directoy you can found a docker-compose.yml. This file get up and running all infraestructure and
services.

To make it, you only need to:
```bash
$> docker-compose build
$> docker-compose up
```

## Development

If you want to get up some individual service, you need to specify active profile: dev

```bash
$> java -Dspring.profiles.active=dev -jar target/service.jar
```


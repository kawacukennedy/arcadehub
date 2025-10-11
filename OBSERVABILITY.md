# Observability

## Logging
- JSON format for structured logging.
- Levels: INFO for production.
- Rotation: Daily, compress older.

## Metrics
- Micrometer with Prometheus registry.
- Endpoint: /metrics
- Key metrics: tick_duration_ms, active_lobbies, connected_players, cheat_violations.

## Tracing
- OpenTelemetry for spans on netty.recv, game.tick, db.query.

## Alerting
- Prometheus rules for high tick latency, cheat spikes, DB issues.
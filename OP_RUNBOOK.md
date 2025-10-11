# Operational Runbook

## Daily Checks
- Check /metrics for active_lobbies and connected_players.
- Verify Prometheus scrape success.
- Check disk usage in /var/lib/arcadehub/replays.

## Incident Response
### High Tick Latency
1. Check arcadehub_tick_duration_ms histogram.
2. Check DB slow queries and lock contention.
3. Scale server replicas or increase threads.
4. Tune GC if pause > 200ms.

### Cheat Spike
1. Identify users from cheat_logs.
2. Replay NDJSON replay.
3. Ban if confirmed.

## Backup
- Postgres: pg_dump daily to S3, retain 30 days.
- Replays: Archive to S3 on end, retain 365 days for flagged.

## SLOs
- Uptime: 99.5% monthly.
- Tick latency: P95 < 55ms.
- Response time: Ack critical alerts < 15min.
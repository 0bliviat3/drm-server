## DRM File Header Interface

| Field     | Length | Type | Description      |
|-----------|--------|------|------------------|
| signature | 12     | byte | DRM magic number |
| key       | 256    | byte | 암호화된 키           |
| iv        | 12     | byte | 암호화에 사용된 iv      |

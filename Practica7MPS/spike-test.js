import http from 'k6/http';
import { check } from 'k6';

export let options = {
  stages: [
    { duration: '2m', target: 40000 },
  ],
  thresholds: {
    http_req_failed: ['rate<=0.005'],
  },
};

export default function () {
  let res = http.get('http://localhost:8080/medico/1');
  check(res, {
    'status was 200': (r) => r.status == 200,
  });
}

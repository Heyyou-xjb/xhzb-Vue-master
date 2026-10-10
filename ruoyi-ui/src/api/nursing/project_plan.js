import request from '@/utils/request'

// 查询护理计划和项目关联列表
export function listProject_plan(query) {
  return request({
    url: '/nursing/project_plan/list',
    method: 'get',
    params: query
  })
}

// 查询护理计划和项目关联详细
export function getProject_plan(id) {
  return request({
    url: '/nursing/project_plan/' + id,
    method: 'get'
  })
}

// 新增护理计划和项目关联
export function addProject_plan(data) {
  return request({
    url: '/nursing/project_plan',
    method: 'post',
    data: data
  })
}

// 修改护理计划和项目关联
export function updateProject_plan(data) {
  return request({
    url: '/nursing/project_plan',
    method: 'put',
    data: data
  })
}

// 删除护理计划和项目关联
export function delProject_plan(id) {
  return request({
    url: '/nursing/project_plan/' + id,
    method: 'delete'
  })
}

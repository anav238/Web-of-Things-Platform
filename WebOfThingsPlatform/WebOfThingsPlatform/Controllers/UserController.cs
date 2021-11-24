using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebOfThingsPlatform.Entities;

namespace WebOfThingsPlatform.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class UserController : ControllerBase
    {
        private readonly ILogger<UserController> _logger;
        public UserController(ILogger<UserController> logger)
        {
            _logger = logger;
        }


        [HttpGet]
        public IEnumerable<User> GetAllUsers()
        {

            return  Enumerable.Range(1, 5).Select(index => new User
            {
                Id = Guid.NewGuid(),
                Username = "random",
                Password = "pasword",
                HomeId = new List<Guid> { Guid.NewGuid() }
            }).ToArray();
        }

        [HttpGet("{id}")]
        public User GetUserById(Guid id)
        {
            return new User
            {
                Id = Guid.NewGuid(),
                Username = "random",
                Password = "pasword",
                HomeId = new List<Guid> { Guid.NewGuid() }
            };
        }

        [HttpPost]
        public ActionResult CreateUser(User user)
        {
            var s = new User
            {
                Id = Guid.NewGuid(),
                Username = "random",
                Password = "pasword",
                HomeId = new List<Guid> { Guid.NewGuid() }
            };
            return Ok(user);
        }

        [HttpPut("{id}")]
        public ActionResult UpdateUser(Guid id, User user)
        {
            return Ok(user);
        }

        [HttpDelete("{id}")]
        public ActionResult DeleteUser(Guid id)
        {
            return NoContent();
        }

        [HttpPost("authenticate")]
        [AllowAnonymous]
        public ActionResult Authenticate(string Username, string Password)
        {
            return Ok("Authenticated");
        }

    }
}


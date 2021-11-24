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
    public class HomeController : ControllerBase
    {
        private readonly ILogger<HomeController> _logger;
        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        [HttpGet]
        public IEnumerable<Home> GetAllHomes()
        {

            return Enumerable.Range(1, 5).Select(index => new Home
            {
                Id = Guid.NewGuid(),
                Name = "random",
                UserId = new List<Guid> { Guid.NewGuid() },
                RoleId = new List<Guid> { Guid.NewGuid() },
                DeviceId = new List<Guid> { Guid.NewGuid() }
            }).ToArray();
        }

        [HttpGet("{id}")]
        public Home GetHomeById(Guid id)
        {
            return new Home
            {
                Id = Guid.NewGuid(),
                Name = "random",
                UserId = new List<Guid> { Guid.NewGuid() },
                RoleId = new List<Guid> { Guid.NewGuid() },
                DeviceId = new List<Guid> { Guid.NewGuid() }
            };
        }

        [HttpGet("allHomesForUser/{id}")]
        public IEnumerable<Home> GetAllHomesByUserId(Guid id)
        {
            return Enumerable.Range(1, 5).Select(index => new Home
            {
                Id = Guid.NewGuid(),
                Name = "random",
                UserId = new List<Guid> { Guid.NewGuid() },
                RoleId = new List<Guid> { Guid.NewGuid() },
                DeviceId = new List<Guid> { Guid.NewGuid() }
            }).ToArray();
        }


        [HttpPost]
        public ActionResult CreateHome(Home home)
        {
            var s = new Home
            {
                Id = Guid.NewGuid(),
                Name = "random",
                UserId = new List<Guid> { Guid.NewGuid() },
                RoleId = new List<Guid> { Guid.NewGuid() },
                DeviceId = new List<Guid> { Guid.NewGuid() }
            };
            return Ok(home);
        }

        [HttpPut("{id}")]
        public ActionResult UpdateHome(Guid id, Home home)
        {
            return Ok(home);
        }

        [HttpPut("userPermission{id}")]
        public ActionResult UpdateUserPermision(Guid HomeId, Guid UserId, string permission)
        {
            return Ok(permission);
        }

        [HttpDelete("{id}")]
        public ActionResult DeleteHome(Guid id)
        {
            return NoContent();
        }

    }
}
